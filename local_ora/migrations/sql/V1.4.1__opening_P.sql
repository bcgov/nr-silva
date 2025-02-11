
  CREATE OR REPLACE EDITIONABLE PACKAGE "THE"."MOF_SPATIAL_VALIDATION" IS

  -- Author  : David Zwiers, Vivid Solutions Inc.

  -- Purpose : Intended to assist in the validation of MDSYS.SDO_GEOMETRY objects,
  -- this package is intended for use as part of strategy for managing assumptions
  -- about the validity of spatial data within MOFR's Operational database. These
  -- routines are safe to execute at any time, but are targeted towards update /
  -- insert triggers.

  -- Revision:
     -- 1.0   Nov. 2006, Targets 10G r2.


  -- The SRID for BC Albers provided by Oracle in 10G
  C_BCALBERS_SRID               CONSTANT NUMBER := 3005;

  -- The SRID for BC Albers provided by Oracle in 9i
  C_BCALBERS_LEGACY_SRID        CONSTANT NUMBER := 1000003005;

  -- Represents the tolerance used by most BC Albers layers
  -- UNITS: METERS
  C_BCALBERS_SDO_TOLERANCE      CONSTANT NUMBER := 0.0005;

  -- The error number used when raising a validation Exception
  C_SPATIAL_VALIDATION_ERROR    CONSTANT NUMBER := -20000;

  /**
   * Validates the geometry according to Oracle Spatial
   *
   * Will throw a spatial Exception on invalid geometries.
   *
   * Uses:
   *      sdo_geom.validate_geometry_with_context
   */
  PROCEDURE VALIDATE ( GEOMETRY IN MDSYS.SDO_GEOMETRY, TOLERANCE IN NUMBER );

  /**
   * Validates the geometry according to Oracle Spatial. When validation fails,
   * an attempt is made to fix the errors. If the errors cannot be fixed without
   * altering the topology, an Exception is thrown.
   *
   * Will throw a spatial Exception on invalid geometries which cannot be fixed.
   *
   * Uses:
   *      sdo_geom.validate_geometry_with_context
   *      sdo_util.rectify_geometry
   */
  PROCEDURE VALIDATE_AND_FIX ( GEOMETRY IN OUT MDSYS.SDO_GEOMETRY, TOLERANCE IN NUMBER);

END MOF_SPATIAL_VALIDATION;
/
CREATE OR REPLACE EDITIONABLE PACKAGE BODY "THE"."MOF_SPATIAL_VALIDATION" IS

  PROCEDURE VALIDATE ( GEOMETRY IN MDSYS.SDO_GEOMETRY, TOLERANCE IN NUMBER) IS
    ERROR_MSG VARCHAR2 ( 2048 );
  BEGIN
     IF GEOMETRY IS NULL THEN
       RETURN;
     END IF;

    ERROR_MSG := SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT ( GEOMETRY, TOLERANCE );
    IF ERROR_MSG <> 'TRUE' THEN
      -- the string is formated to start with the oracle numeric code in 10G,
      -- so add the ORA- prefix to make this clear to the user
      DBMS_STANDARD.RAISE_APPLICATION_ERROR ( C_SPATIAL_VALIDATION_ERROR,'ORA-'||ERROR_MSG );
    END IF;
  END;

  PROCEDURE VALIDATE_AND_FIX ( GEOMETRY in out MDSYS.SDO_GEOMETRY, TOLERANCE in NUMBER) IS
    V_GEOMETRY MDSYS.SDO_GEOMETRY;
  BEGIN
     IF GEOMETRY IS NULL THEN
       RETURN;
     END IF;

      -- only try to fix 'bad' shapes.
      IF SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT ( GEOMETRY, TOLERANCE ) <> 'TRUE' THEN
          V_GEOMETRY := GEOMETRY;
        BEGIN
          -- long narrow polygons may be converted into linestrings ... which is really an error
          GEOMETRY := SDO_UTIL.RECTIFY_GEOMETRY ( GEOMETRY, TOLERANCE );
          IF GEOMETRY IS NULL OR V_GEOMETRY.SDO_GTYPE <> GEOMETRY.SDO_GTYPE THEN
            GEOMETRY := V_GEOMETRY;
          END IF;
        EXCEPTION WHEN OTHERS THEN
          -- it didn't fix itself ... so fail with an error string
          GEOMETRY := V_GEOMETRY;
        END;
        -- throws exception on bad polygons
        VALIDATE ( GEOMETRY, TOLERANCE );
      END IF;
  END;

BEGIN
  -- Initialization
  NULL;
END MOF_SPATIAL_VALIDATION;
/

  CREATE OR REPLACE EDITIONABLE PACKAGE "THE"."RESULTS_VRIMS_EVENT_GEN" AS

--
--     Package:   RESULTS_VRIMS_EVENT_GEN
--     Purpose:   When modifications are made to the identified DB objects, this will result in
--                the generation of a DB trigger events. In response to these events the
--                RESULTS-VRIMS EVENT GENERATOR will generate one and only one XML message
--                for an Opening (having the Opening ID) per Database connection session.

--     Revision History
--     Person               Date       Comments
--     -----------------    ---------  --------------------------------
--     Charles Devjayanth   2012-02-15 Created.
--     Charles Devjayanth   2012-03-06 Uses RESULTS_VRIMS_OID_TEMP for caching
--                                     Opening ID in Transaction.
--

    PROCEDURE generate_vrims_event(opening_id in varchar2);

end RESULTS_VRIMS_EVENT_GEN;
/
CREATE OR REPLACE EDITIONABLE PACKAGE BODY "THE"."RESULTS_VRIMS_EVENT_GEN" AS

    -- check to see if opening exist in temp table
    FUNCTION opening_exists(p_opening_id IN VARCHAR2) RETURN BOOLEAN
    IS
      v_temp_num    NUMBER(1);
    BEGIN
      SELECT COUNT(1) INTO v_temp_num
      FROM RESULTS_VRIMS_OID_TEMP
      WHERE opening_id = p_opening_id;
      RETURN (v_temp_num > 0);
    END opening_exists;

    -- set opening in temp table
    PROCEDURE set_opening_id(p_opening_id IN VARCHAR2)
    IS
    BEGIN
       INSERT INTO RESULTS_VRIMS_OID_TEMP (opening_id) VALUES (p_opening_id);
    END set_opening_id;

   /*
    * Generate VRIMS Event - Affected Table Trigger will call this for Each row
    * Uses RESULTS_VRIMS_OID_TEMP
   */
   PROCEDURE generate_vrims_event(opening_id in varchar2)
    IS
    xml_prolog VARCHAR2(50) := '<?xml version="1.0" encoding="UTF-8"?>';
    xml1 VARCHAR2(50) := '<ResultsVrimsEvent>';
    xml2 VARCHAR2(50) := '<OpeningId>';
    xml3 VARCHAR2(50) := '</OpeningId>';
    xml4 VARCHAR2(50) := '<TimeStamp>';
    xml5 VARCHAR2(50) := '</TimeStamp>';
    xml6 VARCHAR2(50) := '</ResultsVrimsEvent>';
    xml_msg VARCHAR2(200);

    ts VARCHAR2(50);

    BEGIN

      IF opening_id IS NOT NULL AND NOT opening_exists(opening_id) THEN

        -- prepare XML and send event
        ts := TRIM(TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS'));
        xml_msg := xml_prolog || xml1 || xml2 || opening_id || xml3 || xml4 || ts || xml5 || xml6;

        -- Call procedure
        RESULTS_ENQUEUE_MSG('mof_queues.RESULTS_VRIMS_Q', opening_id, 'RESULTS_VRIMS', xml_msg);

        set_opening_id(opening_id); -- mark is as sent
      end if;

    END generate_vrims_event;


end RESULTS_VRIMS_EVENT_GEN;
/

  CREATE OR REPLACE EDITIONABLE PACKAGE "THE"."RESULTS_ILRR_ACQR_PLUGIN_PKG" 
IS
--
--    Package:    RESULTS_ILRR_ACQR_PLUGIN
--
--    Purpose:    Populates the ILRR advanced queue when RESULTS Opening
--                changes are made
--
--    Revision History
--
--    Person             Date       Comments
--    -----------------  ---------  --------------------------------
--    P. Stinson         03/14/2007 created original
--    R. Pardo Figueroa  05/25/2007 Include control of exception to
--                                  avoid propagation to RESULTS
--
--
  PROCEDURE mainline(
    p_transaction_type               IN       VARCHAR2
  , p_opening_id                     IN       VARCHAR2
  , p_op_scope                       IN       VARCHAR2);

END results_ilrr_acqr_plugin_pkg;
/
CREATE OR REPLACE EDITIONABLE PACKAGE BODY "THE"."RESULTS_ILRR_ACQR_PLUGIN_PKG" 
AS
--
-- Purpose:   Message_to_queue
-- Notes:     Populates the Queue
--
-- REVISIONS:
-- Ver        Date        Author           Description
-- ---------  ----------  ---------------  ------------------------------------
-- 01_00_00   03/14/2007  P. Stinson        Created Original
--           05/25/2007  R. Pardo Figueroa Include Exception
--
  PROCEDURE message_to_queue(
    p_action                         IN       VARCHAR2
  , p_op_type                        IN       VARCHAR2
  , p_opening_id                     IN       VARCHAR2)
  IS
    v_enqueue_options                    DBMS_AQ.enqueue_options_t;
    v_message_properties                 DBMS_AQ.message_properties_t;
    v_recipients                         DBMS_AQ.aq$_recipient_list_t;
    v_message_handle                     RAW(16);
    v_message                            results_ilrr_event_typ;
  BEGIN
    v_message := results_ilrr_event_typ(p_action, p_op_type, p_opening_id, '');
    DBMS_AQ.enqueue(queue_name => 'MOF_QUEUES.RESULTS_ILRR_EVENT_Q'
                  , enqueue_options => v_enqueue_options
                  , message_properties => v_message_properties
                  , payload => v_message
                  , msgid => v_message_handle);
  exception
    when others then null;
  END;

--
-- Purpose:   Mainline
-- Notes:     Interface for the package.
--
-- REVISIONS:
-- Ver        Date        Author           Description
-- ---------  ----------  ---------------  ------------------------------------
-- 01_00_00   03/14/2007  P. Stinson       Created Original
--
  PROCEDURE mainline(
    p_transaction_type               IN       VARCHAR2
  , p_opening_id                     IN       VARCHAR2
  , p_op_scope                       IN       VARCHAR2)
  IS
  BEGIN
      --call to construct event queue record
      message_to_queue(p_transaction_type, p_op_scope, p_opening_id);
  END mainline;
END results_ilrr_acqr_plugin_pkg;
/
