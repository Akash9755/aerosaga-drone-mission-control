import { useEffect, useRef, useState } from 'react'
import {
    Viewer,
    Cartesian3,
    Color,
    VerticalOrigin,
    ScreenSpaceEventHandler,
    ScreenSpaceEventType,
} from 'cesium'
import 'cesium/Build/Cesium/Widgets/widgets.css'

function MissionMap() {
    const mapRef = useRef(null)
    const [selectedDrone, setSelectedDrone] = useState(null)

    useEffect(() => {
        const viewer = new Viewer(mapRef.current, {
            animation: false,
            timeline: false,
            baseLayerPicker: false,
            geocoder: false,
            homeButton: false,
            sceneModePicker: false,
            navigationHelpButton: false,
        })

        // AS-001 - Bengaluru
        viewer.entities.add({
            name: 'AS-001',
            position: Cartesian3.fromDegrees(77.5946, 12.9716),
            properties: {
                status: 'Online',
                battery: '92%',
                location: 'Bengaluru',
            },
            point: {
                pixelSize: 14,
                color: Color.GREEN,
                outlineColor: Color.WHITE,
                outlineWidth: 2,
            },
            label: {
                text: 'AS-001',
                font: '14px sans-serif',
                fillColor: Color.WHITE,
                verticalOrigin: VerticalOrigin.BOTTOM,
                pixelOffset: new Cartesian3(0, -10, 0),
            },
        })

        // AS-002 - Mysuru
        viewer.entities.add({
            name: 'AS-002',
            position: Cartesian3.fromDegrees(76.6394, 12.2958),
            properties: {
                status: 'Flying',
                battery: '67%',
                location: 'Mysuru',
            },
            point: {
                pixelSize: 14,
                color: Color.BLUE,
                outlineColor: Color.WHITE,
                outlineWidth: 2,
            },
            label: {
                text: 'AS-002',
                font: '14px sans-serif',
                fillColor: Color.WHITE,
                verticalOrigin: VerticalOrigin.BOTTOM,
                pixelOffset: new Cartesian3(0, -10, 0),
            },
        })

        // AS-003 - Tumakuru
        viewer.entities.add({
            name: 'AS-003',
            position: Cartesian3.fromDegrees(77.1010, 13.3392),
            properties: {
                status: 'Offline',
                battery: '15%',
                location: 'Tumakuru',
            },
            point: {
                pixelSize: 14,
                color: Color.RED,
                outlineColor: Color.WHITE,
                outlineWidth: 2,
            },
            label: {
                text: 'AS-003',
                font: '14px sans-serif',
                fillColor: Color.WHITE,
                verticalOrigin: VerticalOrigin.BOTTOM,
                pixelOffset: new Cartesian3(0, -10, 0),
            },
        })

        // Drone click handler
        const clickHandler = new ScreenSpaceEventHandler(
            viewer.scene.canvas
        )

        clickHandler.setInputAction((click) => {
            const pickedObject = viewer.scene.pick(click.position)

            if (pickedObject && pickedObject.id) {
                const drone = pickedObject.id

                setSelectedDrone({
                    name: drone.name,
                    status: drone.properties.status.getValue(),
                    battery: drone.properties.battery.getValue(),
                    location: drone.properties.location.getValue(),
                })
            }
        }, ScreenSpaceEventType.LEFT_CLICK)

        // Move camera to Karnataka
        viewer.camera.flyTo({
            destination: Cartesian3.fromDegrees(
                77.0,
                12.8,
                150000
            ),
        })

        return () => {
            clickHandler.destroy()
            viewer.destroy()
        }
    }, [])

    return (
        <div style={{ position: 'relative' }}>
            <div
                ref={mapRef}
                style={{
                    width: '100%',
                    height: '480px',
                }}
            />

            {selectedDrone && (
                <div
                    style={{
                        position: 'absolute',
                        top: '20px',
                        right: '20px',
                        background: '#111827',
                        padding: '20px',
                        borderRadius: '10px',
                        color: 'white',
                        minWidth: '200px',
                        boxShadow: '0 4px 15px rgba(0,0,0,0.4)',
                    }}
                >
                    <h3>{selectedDrone.name}</h3>

                    <p>
                        <strong>Status:</strong> {selectedDrone.status}
                    </p>

                    <p>
                        <strong>Battery:</strong> {selectedDrone.battery}
                    </p>

                    <p>
                        <strong>Location:</strong> {selectedDrone.location}
                    </p>

                    <button
                        onClick={() => setSelectedDrone(null)}
                        style={{
                            padding: '8px 15px',
                            borderRadius: '6px',
                            border: 'none',
                            cursor: 'pointer',
                        }}
                    >
                        Close
                    </button>
                </div>
            )}
        </div>
    )
}

export default MissionMap