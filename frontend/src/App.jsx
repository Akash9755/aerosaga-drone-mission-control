import { useState } from 'react'
import DroneCard from './components/DroneCard'
import MissionMap from './MissionMap'
import './App.css'

function App() {

    const [missionStatus, setMissionStatus] = useState('Active')
    const [missionAction, setMissionAction] = useState('Navigate')
    const [missionProgress, setMissionProgress] = useState(50)
    const [showAlerts, setShowAlerts] = useState(true)

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

    const totalDrones = drones.length

    const onlineDrones = drones.filter(
        (drone) => drone.status === 'Online'
    ).length

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
                        <strong>
                            {missionStatus === 'Aborted' ? 0 : 1}
                        </strong>
                    </div>

                    <div className="stat-card">
                        <span>Online Drones</span>
                        <strong>{onlineDrones}</strong>
                    </div>

                </section>

                {/* Mission Map */}
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
                            <DroneCard
                                key={drone.name}
                                drone={drone}
                            />
                        ))}

                    </div>

                </section>

                {/* Recent Alerts */}
                <section className="alerts">

                    <div className="section-header">

                        <h3>Recent Alerts</h3>

                        <button
                            onClick={() => setShowAlerts(!showAlerts)}
                            className="alert-toggle"
                        >
                            {showAlerts ? 'Hide' : 'View All'}
                        </button>

                    </div>

                    {showAlerts && (
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
                    )}

                </section>

                {/* Active Mission */}
                <section className="missions">

                    <div className="section-header">

                        <h3>Active Missions</h3>

                        <span>
                            {missionStatus === 'Aborted'
                                ? '0 Missions'
                                : '1 Mission'}
                        </span>

                    </div>

                    <div className="mission-card">

                        <div className="mission-info">

                            <h4>Mission #001</h4>

                            <p>Drone: AS-001</p>

                            <strong>{missionStatus}</strong>

                        </div>

                        {/* Mission Progress */}
                        <div className="mission-progress">

                            <div className="progress-header">

                                <span>Mission Progress</span>

                                <strong>
                                    {missionProgress}%
                                </strong>

                            </div>

                            <div className="progress-bar">

                                <div
                                    className="progress-level"
                                    style={{
                                        width: `${missionProgress}%`
                                    }}
                                ></div>

                            </div>

                        </div>

                        {/* Mission Status */}
                        <div className="mission-status">

                            <div>
                                <span>Takeoff</span>

                                <strong className="completed">
                                    Completed
                                </strong>
                            </div>

                            <div>
                                <span>Navigate</span>

                                <strong className="active">

                                    {missionAction === 'Navigate'
                                        ? missionStatus
                                        : 'Completed'}

                                </strong>
                            </div>

                            <div>
                                <span>Drop Package</span>

                                <strong
                                    className={
                                        missionAction === 'Drop Package'
                                            ? 'active'
                                            : 'pending'
                                    }
                                >

                                    {missionAction === 'Drop Package'
                                        ? 'Active'
                                        : 'Pending'}

                                </strong>
                            </div>

                            <div>
                                <span>Return</span>

                                <strong
                                    className={
                                        missionAction === 'Return'
                                            ? 'active'
                                            : 'pending'
                                    }
                                >

                                    {missionAction === 'Return'
                                        ? 'Active'
                                        : 'Pending'}

                                </strong>
                            </div>

                        </div>

                        {/* Mission Controls */}
                        <div className="mission-controls">

                            {/* Start */}
                            <button
                                onClick={() => {
                                    setMissionStatus('Started')
                                    setMissionAction('Navigate')
                                    setMissionProgress(25)
                                }}
                            >
                                ▶ Start Mission
                            </button>

                            {/* Pause */}
                            <button
                                onClick={() => {
                                    setMissionStatus('Paused')
                                }}
                            >
                                ⏸ Pause
                            </button>

                            {/* Resume */}
                            <button
                                onClick={() => {
                                    setMissionStatus('Active')
                                }}
                            >
                                ▶ Resume
                            </button>

                            {/* Abort */}
                            <button
                                onClick={() => {
                                    setMissionStatus('Aborted')
                                    setMissionProgress(0)
                                }}
                            >
                                ✕ Abort
                            </button>

                            {/* Return Home */}
                            <button
                                onClick={() => {
                                    setMissionStatus('Returning Home')
                                    setMissionAction('Return')
                                    setMissionProgress(100)
                                }}
                            >
                                🏠 Return Home
                            </button>

                            {/* Drop Package */}
                            <button
                                onClick={() => {
                                    setMissionAction('Drop Package')
                                    setMissionStatus('Active')
                                    setMissionProgress(75)
                                }}
                            >
                                📦 Drop Package
                            </button>

                        </div>

                    </div>

                </section>

            </main>

        </div>
    )
}

export default App