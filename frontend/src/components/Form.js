import React from "react";

import "./Form.css";

function Form(props) {
  const closeForm = props.onCancel ? props.onCancel : null;

  return (
    <form className="Form">
      <div className="form-topbar">
        <h2>
          New <span className="bold">File</span>
        </h2>
        <span onClick={closeForm} className="close">
          X
        </span>
      </div>
      <div className="form-field">
        <label htmlFor="inputTitle">Title: </label>
        <input id="inputTitle" />
      </div>
      <div className="form-field">
        <label htmlFor="inputDescription">Description: </label>
        <textarea rows="6" id="inputDescription" />
      </div>
      <div className="form-field">
        <input type="file" />
      </div>

      <div className="buttons">
        <input type="submit" value="Save" />
        <button onClick={closeForm}>Cancel</button>
      </div>
    </form>
  );
}

export default Form;
