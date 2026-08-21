import DroneCard from "./components/DroneCard";
import MissionMap from "./MissionMap";
import "./App.css";

function App() {
  const drones = [
    {
      name: "AS-001",
      status: "Online",
      battery: 92,
      location: "Bengaluru",
    },
    {
      name: "AS-002",
      status: "Flying",
      battery: 67,
      location: "Mysuru",
    },
    {
      name: "AS-003",
      status: "Offline",
      battery: 15,
      location: "Tumakuru",
    },
  ];

  const totalDrones = drones.length;

  const onlineDrones = drones.filter(
    (drone) => drone.status === "Online",
  ).length;

  const missions = [];

  return (
    <div className="app">
      {/* Sidebar */}
      <aside className="sidebar">
        <div className="sidebar-brand">
          <span className="brand-icon">🚁</span>
          <div>
            <h1>AeroSaga</h1>
            <p>DRONE MISSION CONTROL</p>
          </div>
        </div>

        <nav className="sidebar-nav">
          <div className="nav-item active">📊 Dashboard</div>
          <div className="nav-item">🚁 Drones</div>
          <div className="nav-item">📋 Missions</div>
          <div className="nav-item">📈 Telemetry</div>
          <div className="nav-item">⚠️ Alerts</div>
          <div className="nav-item">📄 Logs</div>
          <div className="nav-item">⚙️ Settings</div>
          <div className="nav-item">👥 Users</div>
        </nav>

        <div className="sidebar-status">
          <p>🟢 System Status</p>
          <span>All Systems Operational</span>
        </div>
      </aside>

      <div className="main-content">
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
              <strong>{drones.length}</strong>
            </div>

            <div className="stat-card">
              <span>Active Missions</span>
              <strong>1</strong>
            </div>

            <div className="stat-card">
              <span>Online Drones</span>
              <strong>
                {drones.filter((drone) => drone.status === "Online").length}
              </strong>
            </div>
          </section>

          <section className="map-section">
            <div className="section-header">
              <h3>Mission Map</h3>
              <span>Live View</span>
            </div>

            <MissionMap />
          </section>

          {/* Drone Fleet */}
          <section className="drone-fleet">
            <div className="section-header">
              <h2>Drone Fleet</h2>
              <span>{drones.length} Drones</span>
            </div>

            <div className="drone-grid">
              {drones.map((drone) => (
                <DroneCard key={drone.name} drone={drone} />
              ))}
            </div>
          </section>

          {/* Recent Alerts */}
          <section className="alerts">
            <div className="section-header">
              <h3>Recent Alerts</h3>
              <span>View All</span>
            </div>

            <div className="alert-list">
              <div className="alert-item">
                <div>
                  <h4>Low Battery - AS-003</h4>
                  <p>15% battery remaining</p>
                </div>
                <span>2 min ago</span>
              </div>

              <div className="alert-item">
                <div>
                  <h4>Signal Loss - AS-002</h4>
                  <p>No signal for 30s</p>
                </div>
                <span>15 min ago</span>
              </div>

              <div className="alert-item">
                <div>
                  <h4>Drone Offline - AS-003</h4>
                  <p>Drone is currently offline</p>
                </div>
                <span>1 hr ago</span>
              </div>
            </div>
          </section>

          <section className="missions">
            <div className="section-header">
              <h3>Active Missions</h3>
              <span>1 Mission</span>
            </div>

            <div className="mission-card">
              <div className="mission-info">
                <h4>Mission #001</h4>
                <p>Drone: AS-001</p>
              </div>

              <div className="mission-status">
                <div>
                  <span>Takeoff</span>
                  <strong className="completed">Completed</strong>
                </div>

                <div>
                  <span>Navigate</span>
                  <strong className="active">Active</strong>
                </div>

                <div>
                  <span>Drop Package</span>
                  <strong className="pending">Pending</strong>
                </div>

                <div>
                  <span>Return</span>
                  <strong className="pending">Pending</strong>
                </div>
              </div>
            </div>
          </section>

          <section className="mission-progress">
            <div className="section-header">
              <h3>Mission Progress</h3>
              <span>View All</span>
            </div>

            <div className="progress-list">
              <div className="progress-item">
                <div className="progress-info">
                  <span>Survey Area A</span>
                  <strong>75%</strong>
                  <small>AS-001</small>
                </div>

                <div className="progress-bar">
                  <div className="progress-fill" style={{ width: "75%" }}></div>
                </div>
              </div>

              <div className="progress-item">
                <div className="progress-info">
                  <span>Delivery Route B</span>
                  <strong>45%</strong>
                  <small>AS-002</small>
                </div>

                <div className="progress-bar">
                  <div className="progress-fill" style={{ width: "45%" }}></div>
                </div>
              </div>

              <div className="progress-item">
                <div className="progress-info">
                  <span>Mapping Zone C</span>
                  <strong>20%</strong>
                  <small>AS-003</small>
                </div>

                <div className="progress-bar">
                  <div className="progress-fill" style={{ width: "20%" }}></div>
                </div>
              </div>

              <div className="progress-item">
                <div className="progress-info">
                  <span>Inspection Task D</span>
                  <strong>90%</strong>
                  <small>AS-003</small>
                </div>

                <div className="progress-bar">
                  <div className="progress-fill" style={{ width: "90%" }}></div>
                </div>
              </div>
            </div>
          </section>

          <section className="quick-actions">
            <div className="section-header">
              <h3>Quick Actions</h3>
            </div>

            <div className="quick-actions-grid">
              <button className="action-card">
                <span>＋</span>
                <strong>New Mission</strong>
              </button>

              <button className="action-card">
                <span>⇧</span>
                <strong>Upload Waypoints</strong>
              </button>

              <button className="action-card">
                <span>🚁</span>
                <strong>Add Drone</strong>
              </button>

              <button className="action-card">
                <span>🗺️</span>
                <strong>View All Missions</strong>
              </button>
            </div>
          </section>
        </main>
      </div>
    </div>
  );
}

export default App;
