import { useEffect } from "react";
import ReactDOM from "react-dom";

const el = document.createElement("div");
el.classList.add("Modal");

function Modal(props) {
  //componentDidMount
  useEffect(() => {
    const modalRoot = document.getElementById("modal-root");
    modalRoot.appendChild(el);

    //componentWillUnmount
    return () => modalRoot.removeChild(el);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  },[]);

  return ReactDOM.createPortal(props.children, el);
}

export default Modal;
