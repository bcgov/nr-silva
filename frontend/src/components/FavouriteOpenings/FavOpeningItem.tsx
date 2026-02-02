import { Link } from "react-router-dom";
import OpeningBookmarkBtn from "../OpeningBookmarkBtn";
import { OpeningDetailsRoute } from "@/routes/config";

import './styles.scss';

type props = {
  openingId: number;
}

const FavOpeningItem = ({ openingId }: props) => (
  <div
    key={openingId}
    className="fav-open-tile-container"
    id={`favourite-opening-tile-${openingId}`}
    data-testid={`favourite-opening-tile-${openingId}`}
  >
    <OpeningBookmarkBtn openingId={openingId} />
    <Link
      className="fav-open-label"
      to={OpeningDetailsRoute.path!.replace(":openingId", openingId.toString())}
    >
      Opening ID{' '}
      <span className="fav-open-id">{openingId}</span>
    </Link>
  </div>
);

export default FavOpeningItem;
