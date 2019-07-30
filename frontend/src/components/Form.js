import React, { useState, useEffect } from "react";
import FileService from "../services/FileService";
import "./Form.css";
import spinner from "../resources/img/spinner.gif";
import { useAppState, ACTION_CHANGE_PAGE, DEFAULT_PAGE_SIZE } from "../AppContext";

function Form(props) {
  const [, dispatch] = useAppState();
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [file, setFile] = useState(null);
  const [validationErrors, setValidationErrors] = useState([]);
  const [showSpinner, setShowSpinner] = useState(false);

  const closeForm = props.onCancel ? props.onCancel : null;

  useEffect(() => {
    if (props.fileToEdit) {
      setTitle(props.fileToEdit.title);
      setDescription(props.fileToEdit.description);
      setFile(null);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  function validateForm() {
    const errors = [];
    if (!file) {
      errors.push({ field: "file", message: "A file must be selected" });
    }

    if (!title || !title.trim()) {
      errors.push({ field: "title", message: "Title is required" });
    }

    if (!description || !description.trim()) {
      errors.push({ field: "description", message: "Description is required" });
    }

    return errors;
  }

  function updateValidation(event) {
    const fieldName = event.target.name;
    const value = event.target.value;

    if (value) {
      // removing the validation error
      const errors = validationErrors.filter(error => error.field !== fieldName);
      setValidationErrors(errors);
    }
  }

  function saveFile(event) {
    event.preventDefault();

    const errors = validateForm();

    if (errors.length > 0) {
      setValidationErrors(errors);
      return;
    }

    setShowSpinner(true);

    const { name, type } = file;
    const reader = new FileReader();
    reader.readAsDataURL(file);

    reader.addEventListener(
      "load",
      function() {
        const base64EncodedFile = reader.result.split(",")[1];
        const fileService = new FileService();
        let promise = null;

        if (props.fileToEdit) {
          const endpoint = props.fileToEdit._links.self.href;
          promise = fileService.update(endpoint, title, description, base64EncodedFile, name, type);
        } else {
          promise = fileService.save(title, description, base64EncodedFile, name, type);
        }

        promise
          .then(res => fileService.list(0, DEFAULT_PAGE_SIZE))
          .then(response => {
            dispatch({
              type: ACTION_CHANGE_PAGE,
              value: { page: response.page, files: response.files }
            });
            setShowSpinner(false);
            alert("File saved");
            closeForm();
          })
          .catch(error => {
            setShowSpinner(false);
            console.log(error);
          });
      },
      false
    );
  }

  return (
    <form className="Form" onSubmit={saveFile}>
      <div className="form-topbar">
        <h2>
          {props.fileToEdit ? "Edit" : "New"} <span className="bold">File</span>
        </h2>
        <span onClick={closeForm} className="close">
          X
        </span>
      </div>
      <div className="form-field">
        <label htmlFor="inputTitle">Title: </label>
        <input
          name="title"
          onBlur={updateValidation}
          value={title}
          onChange={event => setTitle(event.target.value)}
          id="inputTitle"
        />
        {validationErrors
          .filter(error => error.field === "title")
          .map(error => (
            <div className="error-msg" key="description-error">
              {error.message}
            </div>
          ))}
      </div>
      <div className="form-field">
        <label htmlFor="inputDescription">Description: </label>
        <textarea
          onBlur={updateValidation}
          name="description"
          value={description}
          onChange={event => setDescription(event.target.value)}
          rows="6"
          id="inputDescription"
        />
        {validationErrors
          .filter(error => error.field === "description")
          .map(error => (
            <div className="error-msg" key="description-error">
              {error.message}
            </div>
          ))}
      </div>
      <div className="form-field">
        <input
          name="file"
          onChange={event => {
            setFile(event.target.files[0]);
            updateValidation(event);
          }}
          type="file"
        />
        {validationErrors
          .filter(error => error.field === "file")
          .map(error => (
            <div className="error-msg" key="description-error">
              {error.message}
            </div>
          ))}
      </div>

      <div className="buttons">
        {showSpinner ? (
          <img className="spinner" alt="Loading ..." src={spinner} />
        ) : (
          <div>
            <input className="btnSave" type="submit" value="Save" />
            <button onClick={closeForm}>Cancel</button>
          </div>
        )}
      </div>
    </form>
  );
}

export default Form;
