import React from "react";

import './styles.scss';

type CardTitleProps = {
  title: string;
  id?: string;
  subtitle?: string;
}

const CardTitle = ({ title, id, subtitle }: CardTitleProps) => (
  <section className="card-title-section">
    <h4 className="card-title" id={id}>
      {title}
    </h4>
    {
      subtitle
        ? <h5 className="card-subtitle" >{subtitle}</h5>
        : null
    }
  </section>

)

export default CardTitle;
