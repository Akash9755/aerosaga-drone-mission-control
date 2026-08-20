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
    const alerts = [
      {
        title: "Low Battery - AS-003",
        message: "15% battery remaining",
        time: "2 min ago",
      },
      {
        title: "Signal Loss - AS-002",
        message: "No signal for 30s",
        time: "15 min ago",
      },
      {
        title: "Drone Offline - AS-003",
        message: "Drone is currently offline",
        time: "1 hr ago",
      },
    ];
    const missions = [
      {
        id: "Mission #001",
        drone: "AS-001",
        takeoff: "Completed",
        navigate: "Active",
        dropPackage: "Pending",
        returnStatus: "Pending",
      },
    ];
    const missionProgress = [
      {
        name: "Survey Area A",
        progress: 75,
        drone: "AS-001",
      },
      {
        name: "Delivery Route B",
        progress: 45,
        drone: "AS-002",
      },
      {
        name: "Mapping Zone C",
        progress: 20,
        drone: "AS-003",
      },
      {
        name: "Inspection Task D",
        progress: 90,
        drone: "AS-003",
      },
    ];
    const totalDrones = drones.length;

    const onlineDrones = drones.filter(
      (drone) => drone.status === "Online",
    ).length;
   

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
              <strong>{totalDrones}</strong>
            </div>

            <div className="stat-card">
              <span>Active Missions</span>
              <strong>1</strong>
            </div>

            <div className="stat-card">
              <span>Online Drones</span>
              <strong>{onlineDrones}</strong>
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
              {alerts.map((alert) => (
                <div className="alert-item" key={alert.title}>
                  <div>
                    <h4>{alert.title}</h4>
                    <p>{alert.message}</p>
                  </div>

                  <span>{alert.time}</span>
                </div>
              ))}
            </div>
          </section>

          <section className="missions">
            <div className="section-header">
              <h3>Active Missions</h3>
              <span>1 Mission</span>
            </div>

            {missions.map((mission) => (
              <div className="mission-card" key={mission.id}>
                <div className="mission-info">
                  <h4>{mission.id}</h4>
                  <p>Drone: {mission.drone}</p>
                </div>

                <div className="mission-status">
                  <div>
                    <span>Takeoff</span>
                    <strong className="completed">{mission.takeoff}</strong>
                  </div>

                  <div>
                    <span>Navigate</span>
                    <strong className="active">{mission.navigate}</strong>
                  </div>

                  <div>
                    <span>Drop Package</span>
                    <strong className="pending">{mission.dropPackage}</strong>
                  </div>

                  <div>
                    <span>Return</span>
                    <strong className="pending">{mission.returnStatus}</strong>
                  </div>
                </div>
              </div>
            ))}
          </section>
          <section className="mission-progress">
            <div className="section-header">
              <h3>Mission Progress</h3>
              <span>View All</span>
            </div>

            <div className="progress-list">
              {missionProgress.map((mission) => (
                <div className="progress-item" key={mission.name}>
                  <div className="progress-info">
                    <span>{mission.name}</span>
                    <strong>{mission.progress}%</strong>
                    <small>{mission.drone}</small>
                  </div>

                  <div className="progress-bar">
                    <div
                      className="progress-fill"
                      style={{ width: `${mission.progress}%` }}
                    ></div>
                  </div>
                </div>
              ))}
            </div>
          </section>
        </main>
      </div>
    );
}

export default App