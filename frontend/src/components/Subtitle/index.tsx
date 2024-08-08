import React from 'react';

import './styles.scss';

interface SubtitleProps {
  text: string | React.ReactNode;
  className?: string;
  onClick?: () => void;
}

const Subtitle = ({ text, className, onClick }: SubtitleProps) => (
  <p
    className={className ? `${className} subtitle-section` : 'subtitle-section'}
    onClick={onClick}
  >
    {text}
  </p>
);

export default Subtitle;
