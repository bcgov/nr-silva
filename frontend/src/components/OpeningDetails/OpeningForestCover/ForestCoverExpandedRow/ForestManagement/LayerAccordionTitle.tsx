import React from "react";

type LayerAccordionTitleProps = {
  title: string;
  subtitle?: string;
}

const LayerAccordionTitle = ({ title, subtitle }: LayerAccordionTitleProps) => (
  <div className="layer-accordion-title-container">
    <h4>{title}</h4>
    {
      subtitle
        ? <p>{subtitle}</p>
        : null
    }
  </div>
)

export default LayerAccordionTitle;
