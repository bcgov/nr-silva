import React from 'react';
import './ChartContainer.scss'
import ChartTitle from '../ChartTitle';
import { Button } from '@carbon/react';
import * as Icons from '@carbon/icons-react';

type Props = {
  children?: React.ReactNode;
  title: string;
  description: string;
};

/**
 * Renders a Chart Container component.
 *
 * @param {object} props - Component props
 * @param {React.ReactNode} [props.children] - Optional.  The children of the Chart Container component.
 * @param {string} props.title - The title of the chart.
 * @param {string} props.description - The description of the chart.
 * @returns {JSX.Element} A div element containing the chart container.
 */
function ChartContainer({ children, title, description }: Props): JSX.Element {
  return (
    <div className='chart-container'>
        <div className="d-flex flex-row">
          <ChartTitle title={title} subtitle={description} />
          <div className='my-auto px-2'>
            <Button
              hasIconOnly
              iconDescription="Maximize"
              tooltipposition="bottom"
              kind="ghost"
              onClick={() => console.log('Download Click')}
              renderIcon={Icons.Maximize}
              size="md"
            />
            <Button
              hasIconOnly
              iconDescription="Download options"
              tooltipposition="bottom"
              kind="ghost"
              onClick={() => console.log('Download Click')}
              renderIcon={Icons.OverflowMenuVertical}
              size="md"
            />
          </div>
        </div>
        {children? (
          <div className="content">
            {children}
          </div>
        ) : null}
        
    </div>
  );
}

export default ChartContainer;
