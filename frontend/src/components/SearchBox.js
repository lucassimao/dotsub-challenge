import React, { useEffect, useState } from "react";
import "./SearchBox.css";
import FileService from "../services/FileService";
import { DEFAULT_PAGE_SIZE } from "./App";
import { useAppState } from "../AppContext";

const fileService = new FileService();

function SearchBox() {
  const [{searchFilter}, dispatch] = useAppState();

  function setSearchFilter(value){
      dispatch({type:'setSearchFilter', value})
  }

  useEffect(() => {
    if (searchFilter != null) {
      fileService.list(0, DEFAULT_PAGE_SIZE,searchFilter).then(response => {
        dispatch({type: 'CHANGE_PAGE', value: {page: response.page,files: response.files} });
      });
    }
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [searchFilter]);

  return (
    <div className="SearchBox">
      <input
        onChange={evt => setSearchFilter(evt.target.value)}
        placeholder="Type to filter by name or description ..."
      />
    </div>
  );
}

export default SearchBox;
