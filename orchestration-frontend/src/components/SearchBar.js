import { useState, useRef, useEffect } from "react";
import axios from "axios";
import debounce from "lodash.debounce";
import { useNavigate } from "react-router-dom";

export default function SearchBar() {
  const [query, setQuery] = useState("");
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const searchRef = useRef(null);

  // Function to fetch users from the API
  const fetchUsers = async (searchText) => {
    if (searchText.length < 3) return;
    setLoading(true);
    setError(null);
    try {
      const response = await axios.get(
        `http://localhost:8080/api/users/search?query=${searchText}`
      );
      setUsers(response.data);
    } catch (error) {
      console.error("Error fetching users:", error);
      setError("Error fetching users");
    }
    setLoading(false);
  };

  // Debounce the API call to avoid too many requests
  const debouncedSearch = debounce((text) => fetchUsers(text), 300);

  // Close dropdown when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (searchRef.current && !searchRef.current.contains(event.target)) {
        setUsers([]); // Clear dropdown when clicking outside
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <div ref={searchRef} className="relative w-full max-w-lg mx-auto px-2 sm:px-0">
      <input
        type="text"
        className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-blue-500"
        placeholder="Search users..."
        value={query}
        onChange={(e) => {
          setQuery(e.target.value);
          debouncedSearch(e.target.value);
        }}
      />

      {/* Dropdown List */}
      {(users.length > 0 || loading || error || query.length >= 3) && (
        <ul className="absolute w-full bg-white border rounded-lg shadow-lg mt-2 z-10 transition-all duration-300 ease-in-out opacity-100">
          {/* Show loading indicator */}
          {loading && <li className="p-3 text-blue-500">Loading...</li>}

          {/* Show user results */}
          {!loading &&
            users.map((user) => (
              <li
                key={user.id}
                onClick={() => navigate(`/user/${user.id}`)}
                className="p-3 hover:bg-gray-200 cursor-pointer"
              >
                <span className="font-bold">{query}</span>
                {user.firstName.replace(new RegExp(query, "gi"), "")} {user.lastName} ({user.email})
              </li>
            ))}

          {/* Show "No results found" message when applicable */}
          {!loading && users.length === 0 && query.length >= 3 && (
            <li className="p-3 text-gray-500">No results found</li>
          )}

          {/* Show error message if API fails */}
          {error && <li className="p-3 text-red-500">{error}</li>}
        </ul>
      )}
    </div>
  );
}
