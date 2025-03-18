import React from "react";

import './styles.scss';

type CardTitleProps = {
  title: string;
  id?: string;
}

const CardTitle = ({ title, id }: CardTitleProps) => (
  <h4 className="card-title" id={id}>
    {title}
  </h4>
)

export default CardTitle;
