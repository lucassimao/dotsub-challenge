import React from "react";
import "./Table.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faEdit,
  faTrash,
  faFileDownload
} from "@fortawesome/free-solid-svg-icons";

const fakeData = [];

for (let i = 0; i < 10; ++i) {
  fakeData.push({
    name: `name ${i}`,
    description: `description ${i}`,
    createdAt: new Date()
  });
}

function Table() {
  return (
    <table className="Table">
      <thead>
        <tr>
          <th>Name</th>
          <th>Description</th>
          <th className="center">Created at</th>
          <th className="center">Options</th>
        </tr>
      </thead>
      <tbody>
        {fakeData.map(row => (
          <tr>
            <td>{row.name}</td>
            <td>{row.description}</td>
            <td className="center">{row.createdAt.toLocaleDateString()}</td>
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
