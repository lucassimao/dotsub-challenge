import {
  faEdit,
  faFileDownload,
  faTrash
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React from "react";
import { DEFAULT_PAGE_SIZE, useAppState,ACTION_CHANGE_PAGE, ACTION_RESET } from "../AppContext";
import FileService from "../services/FileService";
import "./Table.css";

const fileService = new FileService();

function download(rowData) {
  window.open(rowData._links.download.href, "_blank");
}

function Table() {
  const [appState, dispatch] = useAppState();

  const removeFile = row => {
    const confirmation = window.confirm("Sure ?");
    if (confirmation) {
      fileService
        .removeFile(row)
        .then(res => {
          alert("File removed");
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
        })
        .catch(error => {
          console.log(error);
          alert("It was not possible to remove this file");
        });
    }
  };

  return (
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
              <FontAwesomeIcon className="row-option-edit" icon={faEdit} />
              <FontAwesomeIcon
                onClick={() => removeFile(row)}
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
  );
}

export default Table;
