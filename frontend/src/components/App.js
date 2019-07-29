import React, { useState } from "react";
import "./App.css";
import Paginator from "./Paginator";
import Table from "./Table";
import Form from "./Form";
import Modal from "./Modal";
import SearchBox from "./SearchBox";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPlusCircle } from "@fortawesome/free-solid-svg-icons";
import AppContext from "../AppContext";

export const DEFAULT_PAGE_SIZE = 10;

const initialState = {
  page: 0,
  totalEntries: 0,
  pageSize: DEFAULT_PAGE_SIZE,
  files: []
};

function App() {
  const [mustShowForm, setShowForm] = useState(false);

  const showForm = () => setShowForm(true);
  const hideForm = () => setShowForm(false);

  return (
    <div className="App">
      <div className="TopBar">
        <h1>
          File <span className="bold">Details</span>
        </h1>
        <button onClick={showForm} className="btnAddNewFile">
          <FontAwesomeIcon icon={faPlusCircle} />
          &nbsp;New File
        </button>
      </div>
      <AppContext initialState={initialState}>
        <SearchBox />
        <Table />
        <Paginator />
      </AppContext>

      {mustShowForm ? (
        <Modal>
          <Form onCancel={hideForm} />
        </Modal>
      ) : null}
    </div>
  );
}

export default App;
