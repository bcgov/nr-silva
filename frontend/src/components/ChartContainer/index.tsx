import React from 'react';
import SectionTitle from '../SectionTitle';
import './ChartContainer.scss'

type Props = {
  children?: React.ReactNode;
  title:string;
  description:string;
};

export default function ChartContainer({ children, title, description }: Props) {
  return (
    <div className='chart-container'>
        <SectionTitle title={title} subtitle={description} />
        <div className="content">
            {children}
        </div>
    </div>
  );
}
