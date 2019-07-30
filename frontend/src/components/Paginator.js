import React, { useEffect } from "react";
import "./Paginator.css";
import { useAppState } from "../AppContext";
import FileService from "../services/FileService";

const fileService = new FileService();

function Paginator() {
  const [appState, dispatch] = useAppState();

  const setCurrentPage = (idx)=>{
    fileService.list(idx, appState.pageSize).then(response => {
        dispatch({ type: "setPage", value: response.page.number });
        dispatch({ type: "setTotalPages", value: response.page.totalPages });
        dispatch({ type: "setPageSize", value: response.page.size });
        dispatch({
          type: "setTotalElements",
          value: response.page.totalElements
        });
        dispatch({ type: "setFiles", value: response.files });
    });
  }


  useEffect(()=>{
    setCurrentPage(0);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  },[]);

 

  return appState.totalPages > 1 ? (
    <div className="Paginator">
      <span className="info">
        Page nº 
        <span className="bold">
        {` ${appState.page+1} `}
        </span>
        of
        <span id="total-pages" className="bold">
          {` ${appState.totalPages}` }
        </span>
        {" | Total of "}
        <span id="total-entries" className="bold">
          {appState.totalElements}
        </span>
        {" entries "}
      </span>

      <div className="buttons">
        {appState.page > 0 ? <button  onClick={()=>setCurrentPage(appState.page - 1)}>Previous</button> : null}

        {[...Array(appState.totalPages).keys()].map(idx => (
          <button
            onClick={() => setCurrentPage(idx)}
            className={appState.page === idx ? "active" : ""}
            key={idx}
          >
            {idx + 1}
          </button>
        ))}
        {appState.page + 1 < appState.totalPages ? <button onClick={()=>setCurrentPage(appState.page + 1)}>Next</button> : null}
      </div>
    </div>
  ) : null;
}

export default Paginator;
