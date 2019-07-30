import React, { useEffect } from "react";
import "./Paginator.css";
import {
  useAppState,
  ACTION_CHANGE_PAGE,
  ACTION_SHOW_ERROR_NOTIFICATION
} from "../AppContext";
import FileService from "../services/FileService";

const fileService = new FileService();

function Paginator() {
  const [appState, dispatch] = useAppState();

  const setCurrentPage = idx => {
    fileService
      .list(idx, appState.pageSize, appState.searchFilter)
      .then(response => {
        dispatch({
          type: ACTION_CHANGE_PAGE,
          value: { page: response.page, files: response.files }
        });
      })
      .catch(error => {
        dispatch({
          type: ACTION_SHOW_ERROR_NOTIFICATION,
          value: "It was not possible to obtain the files: " + error.message
        });
      });
  };

  useEffect(() => {
    setCurrentPage(0);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return appState.totalPages > 1 ? (
    <div className="Paginator">
      <span className="info">
        Page nº
        <span className="bold">{` ${appState.page + 1} `}</span>
        of
        <span id="total-pages" className="bold">
          {` ${appState.totalPages}`}
        </span>
        {" | Total of "}
        <span id="total-entries" className="bold">
          {appState.totalElements}
        </span>
        {" entries "}
      </span>

      <div className="buttons">
        {appState.page > 0 ? (
          <button onClick={() => setCurrentPage(appState.page - 1)}>
            Previous
          </button>
        ) : null}

        {[...Array(appState.totalPages).keys()].map(idx => (
          <button
            onClick={() => setCurrentPage(idx)}
            className={appState.page === idx ? "active" : ""}
            key={idx}
          >
            {idx + 1}
          </button>
        ))}
        {appState.page + 1 < appState.totalPages ? (
          <button onClick={() => setCurrentPage(appState.page + 1)}>
            Next
          </button>
        ) : null}
      </div>
    </div>
  ) : null;
}

export default Paginator;
