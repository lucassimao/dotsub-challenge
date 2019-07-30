import React, { createContext, useContext, useReducer } from "react";


function appReducer(state, action) {
  switch (action.type) {
    case "CHANGE_PAGE":
        const {page:{number:page,totalPages,size: pageSize,totalElements},files} = action.value;
        return { ...state, page,totalPages,pageSize,totalElements,files };
    case "setPage":
      return { ...state, page: action.value };
    case "setTotalPages":
      return { ...state, totalPages: action.value };
    case "setTotalElements":
      return { ...state, totalElements: action.value };
    case "setPageSize":
      return { ...state, pageSize: action.value };
    case "setFiles":
      return { ...state, files: action.value };
      case "setSearchFilter":
        return { ...state, searchFilter: action.value };      
    default:
      return state;
  }
}

const Context = createContext([
  {
    pageSize: 0,
    totalPages: 0,
    totalElements: 0,
    searchFilter:null,
    page: 0,
    files: []
  },
  () => {}
]);

function AppContext({ initialState, children }) {
  return (
    <Context.Provider value={useReducer(appReducer, initialState)}>
      {children}
    </Context.Provider>
  );
}

export const useAppState = () => useContext(Context);
export default AppContext;
