import { useEffect } from "react";
import ReactDOM from "react-dom";

const modalRoot = document.getElementById("modal-root");
const el = document.createElement("div");
el.classList.add("Modal");

function Modal(props) {
  //componentDidMount
  useEffect(() => {
    modalRoot.appendChild(el);

    //componentWillUnmount
    return () => modalRoot.removeChild(el);
  });

  return ReactDOM.createPortal(props.children, el);
}

export default Modal;
