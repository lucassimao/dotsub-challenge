import React, { useState } from "react";
import "./Paginator.css";

function Paginator() {
  const [pageSize, setPageSize] = useState(0);
  const [totalEntries] = useState(0);

  return (
    <div className="Paginator">
      <span className="info">
        Showing <span className="bold">{pageSize}</span> out of{" "}
        <span className="bold">{totalEntries}</span> entries
      </span>

      <div className="buttons">
        <button>Previous</button>
        <button className="active">1</button>
        <button>2</button>
        <button>3</button>
        <button>Next</button>
      </div>
    </div>
  );
}

export default Paginator;
