import Lottie from "react-lottie";
export default function LottieLoader({animationData}:any) {
    const defaultOptions = {
      loop: true,
      autoplay: true,
      animationData: animationData,
      rendererSettings: {
        preserveAspectRatio: "xMidYMid slice"
      }
    };
  
    return (
        <Lottie options={defaultOptions} height={'100%'} width={'100%'} />
    );
  }