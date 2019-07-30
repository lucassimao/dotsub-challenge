import "./DialogBox.css";
import React from "react";

function DialogBox(props) {
  const { onOK, onCancel, message } = props;

  return (
    <div className="DialogBox">
      <h3 className="header">Confirmation</h3>
      <p className="body">{message}</p>
      <div className="buttons">
        <button onClick={onOK}>OK</button>
        <button onClick={onCancel}>Cancel</button>
      </div>
    </div>
  );
}

export default DialogBox;
