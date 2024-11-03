function addLocation(button) {
    const data = {
        name: button.dataset.name,
        latitude: button.dataset.latitude,
        longitude: button.dataset.longitude,
        country: button.dataset.country,
        state: button.dataset.state
    };

    fetch('api/locations', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.status === 401) {
                window.location.href = '/login';
                return;
            }
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            const switchContainer = button.closest('[class*="action-container"]');
            switchContainer.setAttribute('th:switch', 'true');
            button.style.display = 'none';
            const badge = switchContainer.querySelector('.badge');
            if (badge) {
                badge.style.display = 'block';
            }
            console.log('Location added successfully');
        })
        .catch(error => {
            console.error('Error adding location:', error);
            showErrorNotification('Failed to add location');
        });
}

function removeLocation(button) {
    const locationName = button.getAttribute('data-name');
    const locationId = button.getAttribute('data-id');

    fetch(`/api/locations/${locationId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.status === 401) {
                window.location.href = '/login';
                return;
            }
            if (response.ok) {
                const card = button.closest('.location-card');
                if (card) {
                    card.remove();
                }
            } else {
                console.error('Failed to remove location');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function showErrorNotification(message) {
    const notification = document.createElement('div');
    notification.className = 'alert alert-danger position-fixed top-0 end-0 m-3';
    notification.style.zIndex = '1050';
    notification.innerHTML = `<i class="fas fa-exclamation-circle me-2"></i>${message}`;
    document.body.appendChild(notification);
    setTimeout(() => {
        notification.remove();
    }, 3000);
}