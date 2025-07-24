import React from "react";

import './styles.scss';
import { CheckmarkFilled } from "@carbon/icons-react";


type ModalTileButtonProps = {
  icon: React.ReactNode
  id: string;
  title: string;
  subtitle?: string;
  selected?: string;
  onClick?: (key: string) => void;
}

const ModalTileButton = (
  { icon, title, subtitle, selected, onClick, id }: ModalTileButtonProps
) => {


  return (
    <button
      id={id}
      type="button"
      className={`modal-tile-button${selected ? ' selected' : ''}`}
      onClick={() => onClick?.(id)}
    >
      <div className="tile-button-content">
        <div className="icon-row">
          {icon}
          {selected ? <CheckmarkFilled /> : null}
        </div>
        <h5 className="tile-title">
          {title}
        </h5>
        <p className="tile-subtitle">
          {subtitle}
        </p>
      </div>
    </button>
  )
}

export default ModalTileButton;
