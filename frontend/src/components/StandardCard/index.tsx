import { useNavigate } from 'react-router-dom';
import { Tile } from '@carbon/react';
import * as Pictograms from '@carbon/pictograms-react';

import './style.scss';

interface StandardCardProps {
  header: string;
  description: string;
  url: string;
  image: string;
}

const StandardCard = ({
  header, description, url, image
}: StandardCardProps) => {
  const navigate = useNavigate();
  const Image = Pictograms[image];
  return (
    <Tile className="std-card-main" onClick={() => navigate(url)}>
      <div className="std-card-header">
        <div>
          <p className="std-card-title">{header}</p>
          <div className="std-card-description">
            <p>{description}</p>
          </div>
          
          <Image className="std-card-pictogram" />
        </div>
      </div>
    </Tile>
  );
};

export default StandardCard;