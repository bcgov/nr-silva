import React from 'react';
import SectionTitle from '../SectionTitle';
import './ChartContainer.scss'
import ChartTitle from '../ChartTitle';
import { Button } from '@carbon/react';
import * as Icons from '@carbon/icons-react';

type Props = {
  children?: React.ReactNode;
  title:string;
  description:string;
};

export default function ChartContainer({ children, title, description }: Props) {
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
        <div className="content">
            {children}
        </div>
    </div>
  );
}
