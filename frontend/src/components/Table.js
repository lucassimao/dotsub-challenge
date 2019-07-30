import {
  faEdit,
  faFileDownload,
  faTrash
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { useState } from "react";
import {
  ACTION_CHANGE_PAGE,
  ACTION_RESET,
  DEFAULT_PAGE_SIZE,
  ACTION_SHOW_INFO_NOTIFICATION,
  ACTION_SHOW_ERROR_NOTIFICATION,
  useAppState
} from "../AppContext";
import FileService from "../services/FileService";
import Form from "./Form";
import Modal from "./Modal";
import "./Table.css";
import DialogBox from "./DialogBox";

const fileService = new FileService();

function download(rowData) {
  window.open(rowData._links.download.href, "_blank");
}

function Table() {
  const [appState, dispatch] = useAppState();
  const [fileToEdit, setFileToEdit] = useState(null);
  const [fileToRemove, setFileToRemove] = useState(null);

  const removeFile = () => {
    fileService
      .removeFile(fileToRemove)
      .then(res => {
        const pageToLoad =
          appState.files.length === 1 ? appState.page - 1 : appState.page;
        if (pageToLoad >= 0) {
          fileService
            .list(pageToLoad, DEFAULT_PAGE_SIZE, appState.searchFilter)
            .then(response => {
              dispatch({
                type: ACTION_CHANGE_PAGE,
                value: { page: response.page, files: response.files }
              });
            });
        } else {
          dispatch({
            type: ACTION_RESET
          });
        }

        dispatch({
          type: ACTION_SHOW_INFO_NOTIFICATION,
          value: "File was successfully removed"
        });
      })
      .catch(error => {
        console.error("error while removing the file ...");
        console.dir(error);

        dispatch({
          type: ACTION_SHOW_ERROR_NOTIFICATION,
          value: "It was not possible to remove the file: " + error.message
        });
      })
      .finally(() => {
        setFileToRemove(null);
      });
  };

  return (
    <React.Fragment>
      <table className="Table">
        <thead>
          <tr>
            <th>Title</th>
            <th>Description</th>
            <th className="center">Created at</th>
            <th className="center">Options</th>
          </tr>
        </thead>
        <tbody>
          {appState.files.map((row, idx) => (
            <tr key={idx}>
              <td>{row.title}</td>
              <td>{row.description}</td>
              <td className="center">{row.dateCreated.toLocaleDateString()}</td>
              <td className="row-options center">
                <FontAwesomeIcon
                  onClick={() => setFileToEdit(row)}
                  className="row-option-edit"
                  icon={faEdit}
                />
                <FontAwesomeIcon
                  onClick={() => setFileToRemove(row)}
                  className="row-option-trash"
                  icon={faTrash}
                />
                <FontAwesomeIcon
                  onClick={() => download(row)}
                  className="row-option-download"
                  icon={faFileDownload}
                />
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {fileToEdit ? (
        <Modal>
          <Form fileToEdit={fileToEdit} onCancel={() => setFileToEdit(null)} />
        </Modal>
      ) : null}

      {fileToRemove ? (
        <Modal>
          <DialogBox
            message="Sure ?"
            onOK={removeFile}
            onCancel={() => setFileToRemove(null)}
          />
        </Modal>
      ) : null}
    </React.Fragment>
  );
}

export default Table;
