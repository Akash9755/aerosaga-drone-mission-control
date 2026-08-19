import DroneCard from './components/DroneCard'
import MissionMap from './MissionMap'
import './App.css'

function App() {

    const drones = [
        {
            name: 'AS-001',
            status: 'Online',
            battery: 92,
            location: 'Bengaluru',
        },
        {
            name: 'AS-002',
            status: 'Flying',
            battery: 67,
            location: 'Mysuru',
        },
        {
            name: 'AS-003',
            status: 'Offline',
            battery: 15,
            location: 'Tumakuru',
        },
    ]
    const totalDrones = drones.length;

    const onlineDrones = drones.filter(
      (drone) => drone.status === "Online",
    ).length;
    const missions = []

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
                <h4>Mission rgb(207, 207, 213)</h4>
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
        </main>
      </div>
    );
}

export default App