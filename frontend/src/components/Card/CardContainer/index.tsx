import React from "react";
import { Grid } from "@carbon/react";

import './styles.scss';

type CardContainerProps = {
  className?: string;
  children?: React.ReactNode;
}

const CardContainer = ({ className, children }: CardContainerProps) => {

  return (
    <Grid className={`card-container-grid default-grid ${className}`}>
      {children}
    </Grid>
  )
}

export default CardContainer;
