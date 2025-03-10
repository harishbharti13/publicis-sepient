import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";

export default function UserDetailsPage() {
  const { id } = useParams();
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    axios
      .get(`http://localhost:8080/api/users/${id}`)
      .then((response) => {
        setUser(response.data);
        setLoading(false);
      })
      .catch((err) => {
        setError("User not found");
        setLoading(false);
      });
  }, [id]);

  if (loading) return <p className="text-center text-gray-500">Loading...</p>;
  if (error) return <p className="text-center text-red-500">{error}</p>;

  return (
    <div className="max-w-lg mx-auto p-6 border rounded-lg shadow-lg bg-white">
      <img
        src={user.image || "https://via.placeholder.com/150"}
        alt="User"
        className="w-32 h-32 mx-auto rounded-full mb-4"
      />
      <h2 className="text-2xl font-bold text-center">
        {user.firstName} {user.lastName}
      </h2>
      <p className="text-gray-600 text-center">{user.email}</p>
      <p className="text-gray-700 text-center">SSN: {user.ssn}</p>
      <button
        onClick={() => window.history.back()}
        className="mt-4 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-700"
      >← Back to Search
</button>
    </div>
  
  );
}
