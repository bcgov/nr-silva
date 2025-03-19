import React from "react";

import './styles.scss';
import { Grid } from "@carbon/react";

type CardContainerProps = {
  children?: React.ReactNode;
}

const CardContainer = ({ children }: CardContainerProps) => {

  return (
    <Grid className="card-container-grid default-grid">
      {children}
    </Grid>
  )
}

export default CardContainer;
