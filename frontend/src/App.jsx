import './App.css'

function App() {
  return (
      <div className="app">
        <header className="header">
          <div className="brand">
            <span className="brand-icon">🚁</span>
            <div>
              <h1>AeroSaga</h1>
              <p>Drone Mission Control</p>
            </div>
          </div>

          <div className="system-status">
            <span className="status-dot"></span>
            System Online
          </div>
        </header>

        <main className="dashboard">
          <section className="welcome">
            <h2>Mission Control Dashboard</h2>
            <p>Monitor and manage autonomous drone missions.</p>
          </section>

          <section className="stats">
            <div className="stat-card">
              <span>Total Drones</span>
              <strong>0</strong>
            </div>

            <div className="stat-card">
              <span>Active Missions</span>
              <strong>0</strong>
            </div>

            <div className="stat-card">
              <span>Online Drones</span>
              <strong>0</strong>
            </div>
          </section>

          <section className="map-section">
            <div className="section-header">
              <h3>Mission Map</h3>
              <span>Live View</span>
            </div>

            <div className="map-placeholder">
              <div className="map-icon">📍</div>
              <h3>Map View</h3>
              <p>Drone mission map will appear here.</p>
            </div>
          </section>

          <section className="missions">
            <div className="section-header">
              <h3>Active Missions</h3>
              <span>0 Missions</span>
            </div>

            <div className="empty-state">
              <p>No active missions</p>
              <small>Mission data will appear here when available.</small>
            </div>
          </section>
        </main>
      </div>
  )
}

export default App