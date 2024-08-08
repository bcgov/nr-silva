import { MapLayer } from "../../types/MapLayer";
import { allLayers } from "../OpeningsMap/constants";

export const generateHtmlFile = (): string => {
  const htmlFile: string[] = [];
  htmlFile.push('<!DOCTYPE html><head><meta charset="UTF-8" /><title>Layers</title></head><body>');
  
  const filtered = allLayers.filter(l => l.name.length > 0);
  if (filtered.length) {
    htmlFile.push(`<h1>${filtered.length} layer(s) currently in use:</h1>`);
    
    htmlFile.push('<p>');
    filtered.forEach((layer: MapLayer) => {
      //htmlFile.push('<li>');
      // name
      htmlFile.push(`#${layer.position} - <strong>${layer.name}</strong> -- ${layer.layers}<br>`);
      // styles
      htmlFile.push('Styles: ');
      layer.styles.forEach((style : {name: string, title: string}) => {
        htmlFile.push(`${style.name} - ${style.title}`);
        if (layer.styles.length > 1) {
          htmlFile.push(', ');
        }
      });
      htmlFile.push('<br>');
      // catalogue url
      htmlFile.push(`<a href="${layer.catalogueUrl}">Dataset here</a> `);
      // Get Capabilities url
      htmlFile.push(`and <a href="${layer.getCapabilitiesUrl}">Get Capabilities here</a><br><br>`);
      //htmlFile.push('</li>');
      
    });
    htmlFile.push('</p>');
  }
  
  htmlFile.push('</body></html>');
  return htmlFile.join('');
}
