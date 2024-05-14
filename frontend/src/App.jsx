import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import "/node_modules/bootstrap/dist/js/bootstrap.bundle.min.js";

import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import AddRoom from "./components/room/AddRoom";
import ExistingRooms from "./components/room/ExistingRooms";
import Home from "./components/home/Home";
import EditRoom from "./components/room/EditRoom";
import Footer from "./components/layout/Footer";
import NavBar from "./components/layout/Navbar";
import RoomListing from "./components/room/RoomListing";
import Admin from "./components/admin/Admin";
import BookingSuccess from "./components/booking/BookingSucess";
import Bookings from "./components/booking/Bookings";
import FindBooking from "./components/booking/FindBooking";
import Checkout from "./components/booking/Checkout";
import Login from "./components/auth/Login";
import Registration from "./components/auth/Registration";
import Logout from "./components/auth/Logout";
import Profile from "./components/auth/Profile";
import { AuthProvider } from "./components/auth/AuthProvider";


function App() {
  return (
<>
    <AuthProvider>
      <main>
        <Router>
          <NavBar />
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/edit-room/:roomId" element={<EditRoom />} />
            <Route path="/existing-rooms" element={<ExistingRooms />} />
            <Route path="/add-room" element={<AddRoom />} />
            <Route path="/browse-all-rooms" element={<RoomListing />} />
            <Route path="/admin" element={<Admin />} />
            <Route path="/existing-bookings" element={<Bookings />} />
            <Route path="/find-booking" element={<FindBooking />} />
            <Route path="/book-room/:roomId" element={<Checkout />} />
            <Route path="booking-success" element={<BookingSuccess />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Registration />} />
            <Route path="/profile" element={<Profile />} />
            <Route path="/logout" element={<Logout />} /> */
          </Routes>
        </Router>

        <Footer />
      </main>
      </AuthProvider>
      </>
  );
}

export default App;
