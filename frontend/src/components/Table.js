import {
  faEdit,
  faFileDownload,
  faTrash
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React from "react";
import { useAppState } from "../AppContext";
import "./Table.css";

function Table() {
  const [appState] = useAppState();

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
            <td className="center">{new Date(row.dateCreated).toLocaleDateString()}</td>
            <td className="row-options center">
              <FontAwesomeIcon className="row-option-edit" icon={faEdit} />
              <FontAwesomeIcon className="row-option-trash" icon={faTrash} />
              <FontAwesomeIcon
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
