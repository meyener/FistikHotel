import AddRoom from "./components/room/AddRoom"
import "../src/App.css"
import "../node_modules/bootstrap/dist/css/bootstrap.min.css"
import "/node_modules/bootstrap/dist/js/bootstrap.min.js"
import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import Home from "./components/home/Home"
import EditRoom from "./components/room/EditRoom"
import ExistingRooms from "./components/room/ExistingRooms"
import NavBar from "./components/layout/NavBar"

function App() {

  return (
    <>

			<main>
        <NavBar/>
				<Router>
					<Routes>
						<Route path="/" element={<Home />} />
						<Route path="/edit-room/:roomId" element={<EditRoom />} />
						<Route path="/existing-rooms" element={<ExistingRooms />} />
						<Route path="/add-room" element={<AddRoom />} />
					</Routes>
				</Router>
			</main>
    </>
  )
}

export default App
