insert into OPEN_CATEGORY_CODE (
  OPEN_CATEGORY_CODE,
  DESCRIPTION,
  EFFECTIVE_DATE,
  EXPIRY_DATE,
  UPDATE_TIMESTAMP
) values
  ('AAA', 'AAA description valid', '2020-01-01', '9999-12-31', '2024-09-03'),
  ('BBB', 'BBB description expired', '2020-01-01', '2022-12-31', '2022-12-31');
