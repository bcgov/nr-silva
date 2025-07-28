import React from "react";
import { CheckmarkFilled } from "@carbon/icons-react";

import './styles.scss';

type ModalTileButtonProps = {
  icon: React.ReactNode
  id: string;
  title: string;
  subtitle?: string;
  selected?: boolean;
  onClick?: () => void;
}

const ModalTileButton = (
  { icon, title, subtitle, selected, onClick, id }: ModalTileButtonProps
) => (
  <button
    id={id}
    type="button"
    className={`modal-tile-button${selected ? ' selected' : ''}`}
    onClick={onClick}
  >
    <div className="tile-button-content">
      <div className="icon-row">
        <span className="main-icon">
          {icon}
        </span>
        {selected ? <span className="selected-icon"><CheckmarkFilled /></span> : null}
      </div>

      <div className="title-row">
        <h5 className="tile-title">
          {title}
        </h5>
        {
          subtitle
            ? (
              <p className="tile-subtitle">
                {subtitle}
              </p>
            )
            : null
        }
      </div>
    </div>
  </button>
);

export default ModalTileButton;
