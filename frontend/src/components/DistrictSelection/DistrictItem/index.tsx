import React from 'react';
import { AxiosError } from 'axios';
import { Row, Column, ButtonSkeleton } from '@carbon/react';
import * as Icons from '@carbon/icons-react';

import { ClientTypeIconMap } from '../constants';

import { ForestClientDto } from '../../../services/OpenApi';

type DistrictItemProps = {
  client: ForestClientDto,
  preSelected: boolean,
  isLoading?: boolean
}

const DistrictItem = ({ client, preSelected, isLoading }: DistrictItemProps) => {

  const renderIcon = () => {
    const clientIcon = ClientTypeIconMap[client.clientTypeCode.code ?? 'I'];
    let Img = null;
    if (preSelected) {
      Img = Icons.CheckmarkFilled;
      return <Img className="org-item-icon" />;
    }
    if (clientIcon) {
      Img = Icons[clientIcon];

      return <Img className="org-item-icon" />;
    }

    Img = Icons.Help;
    return <Img className="org-item-icon" />;
  };

  if (isLoading) {
    return <ButtonSkeleton />
  }

  return (
    <>
      <span className="icon-and-name-row">
        {
          renderIcon()
        }
        <p className="client-name">{client.name ?? client.clientName}</p>
      </span>
      <span className="sub-info-row">
        {
          `ID ${client.clientNumber}`
        }
      </span>
    </>
  );
};

export default DistrictItem;
