/* KHAN AI TRADER durable localStorage bridge. Loaded before app scripts. */
(function () {
  try {
    var bridge = window.KhanNativeStorage;
    if (bridge && typeof bridge.loadBackup === 'function') {
      var raw = bridge.loadBackup();
      if (raw) {
        var snapshot = JSON.parse(raw);
        if (snapshot && typeof snapshot === 'object') {
          Object.keys(snapshot).forEach(function (key) {
            if (localStorage.getItem(key) === null) localStorage.setItem(key, String(snapshot[key]));
          });
        }
      }
    }
  } catch (e) { console.warn('Khan storage restore skipped', e); }

  var originalSet = Storage.prototype.setItem;
  var originalRemove = Storage.prototype.removeItem;
  var originalClear = Storage.prototype.clear;
  var scheduled = false;
  function persist() {
    if (scheduled) return;
    scheduled = true;
    Promise.resolve().then(function () {
      scheduled = false;
      try {
        if (window.KhanNativeStorage && window.localStorage) {
          var snapshot = {};
          for (var i = 0; i < localStorage.length; i++) {
            var key = localStorage.key(i);
            snapshot[key] = localStorage.getItem(key);
          }
          window.KhanNativeStorage.saveBackup(JSON.stringify(snapshot));
        }
      } catch (e) { console.warn('Khan native backup failed', e); }
    });
  }
  Storage.prototype.setItem = function (key, value) {
    originalSet.call(this, key, value);
    if (this === window.localStorage) persist();
  };
  Storage.prototype.removeItem = function (key) {
    originalRemove.call(this, key);
    if (this === window.localStorage) persist();
  };
  Storage.prototype.clear = function () {
    originalClear.call(this);
    if (this === window.localStorage) persist();
  };
  window.addEventListener('pagehide', persist);
})();
