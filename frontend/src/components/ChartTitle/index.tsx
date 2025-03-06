import { Column } from '@carbon/react';
import Subtitle from '../Subtitle';

import './styles.scss';

interface ChartTitleProps {
  title: string;
  subtitle: string;
  enableFavourite?: boolean;
  activity?: string;
}

/**
 * Renders a Chart Title component.
 *
 * @param {object} props - Component props
 * @param {string} props.title - The title of the chart.
 * @param {string} props.subtitle - The subtitle of the chart.
 * @param {boolean} [props.enableFavourite=false] - Whether to enable the favourite button.
 * @param {string} [props.activity] - The activity of the chart.
 * @returns {React.JSX.Element} A div element containing the chart title.
 */
function ChartTitle({ title, subtitle }: ChartTitleProps) {
  return (
    <div className="chart-title">
      <h1 className="chart-title-header">{title}</h1>
      <Subtitle text={subtitle} />
    </div>
  );
}

export default ChartTitle;
