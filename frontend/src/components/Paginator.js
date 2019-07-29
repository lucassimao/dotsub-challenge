import React, { useState, useEffect } from "react";
import "./Paginator.css";
import { useAppState } from "../AppContext";
import FileService from "../services/FileService";

const fileService = new FileService();

function Paginator() {
  const [appState, dispatch] = useAppState();
  const [currentPage, setCurrentPage] = useState(0);

  useEffect(() => {
    let mounted = true;
    
    fileService.list(currentPage, appState.pageSize).then(response => {
      if (mounted) {
        dispatch({ type: "setPage", value: response.page.number });
        dispatch({ type: "setTotalPages", value: response.page.totalPages });
        dispatch({ type: "setPageSize", value: response.page.size });
        dispatch({
          type: "setTotalElements",
          value: response.page.totalElements
        });
        dispatch({ type: "setFiles", value: response.files });
      }
    });

    return () => {
      mounted = false;
    };

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentPage]);

  // TODO controlar o caso do dataset vazio
  return (
    <div className="Paginator">
      <span className="info">
        Page nº 
        <span className="bold">
        {` ${appState.page+1} of `}
        </span>
        <span id="page-size" className="bold">
          {appState.pageSize}
        </span>
        {" | Total of "}
        <span id="total-entries" className="bold">
          {appState.totalElements}
        </span>
        {" entries "}
      </span>

      <div className="buttons">
        {currentPage > 0 ? <button  onClick={()=>setCurrentPage(currentPage - 1)}>Previous</button> : null}

        {[...Array(appState.totalPages).keys()].map(idx => (
          <button
            onClick={() => setCurrentPage(idx)}
            className={currentPage === idx ? "active" : ""}
            key={idx}
          >
            {idx + 1}
          </button>
        ))}
        {currentPage + 1 < appState.totalPages ? <button onClick={()=>setCurrentPage(currentPage + 1)}>Next</button> : null}
      </div>
    </div>
  );
}

export default Paginator;
