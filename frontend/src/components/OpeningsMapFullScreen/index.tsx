import React, { useEffect, useRef } from "react";
import L from "leaflet";
import { useMap } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import { svgIcon } from "./constants";

interface OpeningsMapFullScreenProps {
  fullscreen: boolean;
  onToggle: () => void;
}

const createDiv = () => {
  return L.DomUtil.create(
    "div",
    "leaflet-control-fullscreen leaflet-bar leaflet-control "
  );
};

const createAnchorButton = (
  container: HTMLDivElement,
  fullscreen: boolean,
  onToggle: () => void
) => {
  const button = L.DomUtil.create(
    "a",
    "leaflet-control-fullscreen-button",
    container
  );
  button.href = "#";
  button.title = fullscreen ? "Restore size" : "Expand size";
  button.role = "button";
  button.ariaLabel = fullscreen ? "Restore size" : "Expand size";
  button.ariaDisabled = "false";

  const iconStyle = `
        fond-size: 22px;
      `;

  (button.style as any).cssText = iconStyle;

  L.DomEvent.on(button, "click", (e: any) => {
    L.DomEvent.stopPropagation(e);
    L.DomEvent.preventDefault(e);
    onToggle();
  });
  return button;
};

const createInnerSpanWithIcon = (button: HTMLAnchorElement) => {
  const span = L.DomUtil.create("span", "", button);
  span.ariaHidden = "true";
  span.innerHTML = svgIcon;
};

const OpeningsMapFullScreen: React.FC<OpeningsMapFullScreenProps> = ({
  onToggle,
  fullscreen,
}) => {
  const map = useMap();
  const controlRef = useRef<L.Control | null>(null);

  useEffect(() => {
    const FullscreenControl = L.Control.extend({
      onAdd: () => {
        const container = createDiv();
        createInnerSpanWithIcon(
          createAnchorButton(container, fullscreen, onToggle)
        );

        return container;
      },
    });

    controlRef.current = new FullscreenControl({ position: "topleft" });
    map.addControl(controlRef.current);

    return () => {
      if (controlRef.current) {
        map.removeControl(controlRef.current);
      }
    };
  }, [map, fullscreen, onToggle]);

  return null;
};

export default OpeningsMapFullScreen;
