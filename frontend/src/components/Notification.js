import "./Notification.css";
import React from "react";

import { useAppState, ACTION_HIDE_NOTIFICATION } from "../AppContext";

function Notification() {
  const [{ notification }, dispatch] = useAppState();

  return notification ? (
    <div className={`Notification ${notification.type}`}>
      {notification.value}
      <button
        onClick={() =>
          dispatch({
            type: ACTION_HIDE_NOTIFICATION
          })
        }
        className="btnNotificationClose"
      >
        X
      </button>
    </div>
  ) : null;
}

export default Notification;
