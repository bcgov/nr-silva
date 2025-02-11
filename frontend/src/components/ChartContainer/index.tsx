import React from 'react';
import { Column, Grid } from '@carbon/react';
import ChartTitle from '../ChartTitle';

import './styles.scss'

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
    <Grid className='chart-container-grid'>
      <Column sm={4} md={8} lg={16}>
        <ChartTitle title={title} subtitle={description} />
      </Column>
      {children}
    </Grid>
  );
}

export default ChartContainer;
