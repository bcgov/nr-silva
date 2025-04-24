import React from "react";
import { MockedDisturbanceDetailType } from "./definitions";

const DisturbanceDetail = ({ detail }: { detail: MockedDisturbanceDetailType }) => {

  return (
    <div>{detail.cutBlock}</div>
  )
}

export default DisturbanceDetail;
