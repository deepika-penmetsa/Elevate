import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  user: null,   // Stores user details
  userClubs: [], // Stores user's joined clubs
};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    setUser: (state, action) => {
      state.user = action.payload;
    },
    setUserClubs: (state, action) => {
      state.userClubs = action.payload;
    },
    logoutUser: (state) => {
      state.user = null;
      state.userClubs = [];
    }
  },
});

export const { setUser, setUserClubs, logoutUser } = userSlice.actions;
export default userSlice.reducer;