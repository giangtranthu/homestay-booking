import axios from "axios"

export const api = axios.create({
    baseURL: "http://localhost:8080"
})

export const getHeader = () => {
	const token = localStorage.getItem("token")
	return {
		Authorization: `Bearer ${token}`,
		"Content-Type": "multipart/form-data"
	}
}

export async function addRoom(photo, roomType, roomPrice) {
    const formData = new FormData()
    formData.append("photo", photo)
    formData.append("roomType", roomType)
    formData.append("roomPrice", roomPrice)

    const response = await api.post("/room/add/new-room", formData, {
		headers: getHeader()
	})

    if (response.status === 201) {
        return true
    }
    else {
        return false
    }
}

// get all room
export async function getRoomType() {
    try {
        const response = await axios.get("http://localhost:8080/room/room/types")
        return response.data 
    }
    catch (error) {
        throw new Error("Error fetching room types")
    }
}

// get all rooms from the database
export async function getAllRooms() {
    try {
        const result = await api.get("/room/all-rooms")
        return result.data
    }
    catch (error) {
        throw new Error("Error fetching rooms")
    }
}

// delete a room by the ID
export async function deleteRoom(roomId) {
    try {
        const result = await api.delete(`/room/delete/room/${roomId}`, {
			headers: getHeader()
		})
        return result.data
    }
    catch (error) {
        throw new Error(`Error deleting room ${error.message}`)
    }
}

// update a room
export async function updateRoom(roomId, roomData) {
    const formData = new FormData()
    formData.append("roomType", roomData.roomType)
    formData.append("roomPrice", roomData.roomPrice)
    formData.append("photo", roomData.photo)
    const response = await api.put(`/room/update/${roomId}`, formData, {
		headers: getHeader()})
    return response
}

// get a room by ID
export async function getRoomById(roomId) {
    try {
        const result = await api.get(`/room/room/${roomId}`)
        return result.data
    }
    catch (error) {
        throw new Error(`Error fetching room${error.message}`)
    }
}


// save new room to databse
export async function bookRoom(roomId, booking) {
	try {
		const response = await api.post(`/booking/room/${roomId}/booking`, booking)
		return response.data
	} catch (error) {
		if (error.response && error.response.data) {
			throw new Error(error.response.data)
		} else {
			throw new Error(`Error booking room : ${error.message}`)
		}
	}
}

// get all booking from database
export async function getAllBookings() {
	try {
		const result = await api.get("/booking/all-bookings", {
			headers: getHeader(),
		  });
		  return result.data
	} catch (error) {
		throw new Error(`Error fetching bookings: ${error.message}`)
	}
}


// get booking by the confirm code
export async function getBookingByConfirmationCode(confirmationCode) {
	try {
		const result = await api.get(`/booking/confirmation/${confirmationCode}`)
		return result.data
	} catch (error) {
		if (error.response && error.response.data) {
			throw new Error(error.response.data)
		} else {
			throw new Error(`Error find booking : ${error.message}`)
		}
	}
}


// cancel booking
export async function cancelBooking(bookingId) {
	try {
		const result = await api.delete(`/booking/booking/${bookingId}/delete`)
		return result.data
	} catch (error) {
		throw new Error(`Error cancelling booking :${error.message}`)
	}
}


// get available room
export async function getAvailableRooms(checkinDate, checkoutDate, roomType) {
	const result = await api.get(
		`room/available-rooms?checkinDate=${checkinDate}
		&checkoutDate=${checkoutDate}&roomType=${roomType}`
	)
	return result
}


// register new user
export async function registerUser(registration) {
	try {
		const response = await api.post("/auth/register-user", registration)
		return response.data
	} catch (error) {
		if (error.reeponse && error.response.data) {
			throw new Error(error.response.data)
		} else {
			throw new Error(`User registration error : ${error.message}`)
		}
	}
}



// login
export async function loginUser(login) {
	try {
		const response = await api.post("/auth/login", login)
		if (response.status >= 200 && response.status < 300) {
			return response.data
		} else {
			return null
		}
	} catch (error) {
		console.error(error)
		return null
	}
}


// user profile
export async function getUserProfile(userId, token) {
	try {
		const response = await api.get(`users/profile/${userId}`, {
			headers: getHeader(),
		  });
		return response.data
	} catch (error) {
		throw error
	}
}


// delete user
export async function deleteUser(userId) {

	try {
		const response = await api.delete(`/user/delete/${userId}`, {
			headers: getHeader(),
		  });
		return response.data
	} catch (error) {
		return error.message
	}
}


// get a single user
export async function getUser(userId, token) {
	try {
		const response = await api.get(`/user/${userId}`, {
			headers: getHeader()
		})
		return response.data
	} catch (error) {
		throw error
	}
}


// get booking user by id
export async function getBookingsByUserId(userId, token) {
	try {
		const response = await api.get(`/booking/user/${userId}/bookings`, {
			headers: getHeader()
		})
		return response.data
	} catch (error) {
		console.error("Error fetching bookings:", error.message)
		throw new Error("Failed to fetch bookings")
	}
}