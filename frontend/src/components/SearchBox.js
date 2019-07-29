import React from "react";
import "./SearchBox.css";

function SearchBox() {
  return (
    <div className="SearchBox">
      <input placeholder="Type to filter by name or description ..." />
    </div>
  );
}

export default SearchBox;
