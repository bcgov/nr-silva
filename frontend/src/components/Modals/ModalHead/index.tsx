import React from "react";

import './styles.scss';

type ModalHeadProps = {
  helperTop?: string;
  title?: string;
  helperBottom?: string;
}

const ModalHead = ({ helperTop, helperBottom, title }: ModalHeadProps) => {
  if (!helperTop && !helperBottom && !title) {
    return null;
  }

  return (
    <section className="modal-title-section" aria-label="modal title section">
      {
        helperTop
          ? <p>{helperTop}</p>
          : null
      }
      {
        title
          ? <h4>{title}</h4>
          : null
      }
      {
        helperBottom
          ? <p>{helperBottom}</p>
          : null
      }
    </section>
  )
};

export default ModalHead;
