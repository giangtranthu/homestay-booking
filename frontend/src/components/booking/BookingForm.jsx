import React, { useEffect } from "react";
import moment from "moment";
import { useState } from "react";
import { Form, FormControl, Button } from "react-bootstrap";
import BookingSummary from "./BookingSummary";
import { bookRoom, getRoomById } from "../utils/APIFunction";
import { useNavigate, useParams } from "react-router-dom";

const BookingForm = () => {
  const [validated, setValidated] = useState(false);
  const [isSubmitted, setIsSubmitted] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [roomPrice, setRoomPrice] = useState(0);

  const currentUser = localStorage.getItem("userId");

  const [booking, setBooking] = useState({
    guestName: "",
    guestEmail: currentUser,
    checkinDate: "",
    checkoutDate: "",
    numGuest: "",
  });

  const { roomId } = useParams();
  const navigate = useNavigate();

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setBooking({ ...booking, [name]: value });
    setErrorMessage("");
  };

  const getRoomPriceById = async (roomId) => {
    try {
      const response = await getRoomById(roomId);
      setRoomPrice(response.roomPrice);
    } catch (error) {
      throw new Error(error);
    }
  };

  useEffect(() => {
    getRoomPriceById(roomId);
  }, [roomId]);

  const calculatePayment = () => {
    const checkinDate = moment(booking.checkinDate);
    const checkoutDate = moment(booking.checkoutDate);
    const diffInDays = checkoutDate.diff(checkinDate, "days");
    const paymentPerDay = roomPrice ? roomPrice : 0;
    return diffInDays * paymentPerDay;
  };

  const isGuestCountValid = () => {
    const guestCount = parseInt(booking.numGuest);
    return guestCount >= 1;
  };

  const ischeckoutDateValid = () => {
    if (
      !moment(booking.checkoutDate).isSameOrAfter(moment(booking.checkinDate))
    ) {
      setErrorMessage("Check-out date must be after check-in date");
      return false;
    } else {
      setErrorMessage("");
      return true;
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const form = e.currentTarget;
    if (
      form.checkValidity() === false ||
      !isGuestCountValid() ||
      !ischeckoutDateValid()
    ) {
      e.stopPropagation();
    } else {
      setIsSubmitted(true);
    }
    setValidated(true);
  };

  const handleFormSubmit = async () => {
    try {
      const confirmationCode = await bookRoom(roomId, booking);
      setIsSubmitted(true);
      navigate("/booking-success", { state: { message: confirmationCode } });
    } catch (error) {
      const errorMessage = error.message;
      console.log(errorMessage);
      navigate("/booking-success", { state: { error: errorMessage } });
    }
  };

  return (
    <>
      <div className="container mb-5">
        <div className="row">
          <div className="col-md-6">
            <div className="card card-body mt-5">
              <h4 className="card-title">Reserve Room</h4>

              <Form noValidate validated={validated} onSubmit={handleSubmit}>
                <Form.Group>
                  <Form.Label htmlFor="guestName" className="hotel-color">
                    Fullname
                  </Form.Label>
                  <FormControl
                    required
                    type="text"
                    id="guestName"
                    name="guestName"
                    value={booking.guestName}
                    placeholder="Enter your fullname"
                    onChange={handleInputChange}
                  />
                  <Form.Control.Feedback type="invalid">
                    Please enter your fullname.
                  </Form.Control.Feedback>
                </Form.Group>

                <Form.Group>
                  <Form.Label htmlFor="guestEmail" className="hotel-color">
                    Email
                  </Form.Label>
                  <FormControl
                    required
                    type="email"
                    id="guestEmail"
                    name="guestEmail"
                    value={booking.guestEmail}
                    placeholder="Enter your email"
                    onChange={handleInputChange}
                    // disabled
                  />
                  <Form.Control.Feedback type="invalid">
                    Please enter a valid email address.
                  </Form.Control.Feedback>
                </Form.Group>

                <fieldset style={{ border: "2px" }}>
                  <div className="row">
                    <div className="col-6">
                      <Form.Label htmlFor="checkinDate" className="hotel-color">
                        Check-in date
                      </Form.Label>
                      <FormControl
                        required
                        type="date"
                        id="checkinDate"
                        name="checkinDate"
                        value={booking.checkinDate}
                        placeholder="check-in-date"
                        min={moment().format("MMM Do, YYYY")}
                        onChange={handleInputChange}
                      />
                      <Form.Control.Feedback type="invalid">
                        Please select a check in date.
                      </Form.Control.Feedback>
                    </div>

                    <div className="col-6">
                      <Form.Label
                        htmlFor="checkoutDate"
                        className="hotel-color">
                        Check-out date
                      </Form.Label>
                      <FormControl
                        required
                        type="date"
                        id="checkoutDate"
                        name="checkoutDate"
                        value={booking.checkoutDate}
                        placeholder="check-out-date"
                        min={moment().format("MMM Do, YYYY")}
                        onChange={handleInputChange}
                      />
                      <Form.Control.Feedback type="invalid">
                        Please select a check out date.
                      </Form.Control.Feedback>
                    </div>
                    {errorMessage && (
                      <p className="error-message text-danger">
                        {errorMessage}
                      </p>
                    )}
                  </div>
                </fieldset>

                <fieldset style={{ border: "2px" }}>
                  <div className="row">
                    <div className="col-6">
                      <Form.Label htmlFor="numGuest" className="hotel-color">
                        Number of guest
                      </Form.Label>
                      <FormControl
                        required
                        type="number"
                        id="numGuest"
                        name="numGuest"
                        value={booking.numGuest}
                        min={1}
                        placeholder="0"
                        onChange={handleInputChange}
                      />
                      <Form.Control.Feedback type="invalid">
                        Please select at least 1 adult.
                      </Form.Control.Feedback>
                    </div>
                    {/* <div className="col-6">
											<Form.Label htmlFor="numOfChildren" className="hotel-color">
												Children
											</Form.Label>
											<FormControl
												required
												type="number"
												id="numOfChildren"
												name="numOfChildren"
												value={booking.numOfChildren}
												placeholder="0"
												min={0}
												onChange={handleInputChange}
											/>
											<Form.Control.Feedback type="invalid">
												Select 0 if no children
											</Form.Control.Feedback>
										</div> */}
                  </div>
                </fieldset>

                <div className="fom-group mt-2 mb-2">
                  <button type="submit" className="btn btn-hotel">
                    Continue
                  </button>
                </div>
              </Form>
            </div>
          </div>

          <div className="col-md-4">
            {isSubmitted && (
              <BookingSummary
                booking={booking}
                payment={calculatePayment()}
                onConfirm={handleFormSubmit}
                isFormValid={validated}
              />
            )}
          </div>
        </div>
      </div>
    </>
  );
};
export default BookingForm;
