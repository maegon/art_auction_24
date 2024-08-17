document.addEventListener('DOMContentLoaded', function() {
            var calendarEl = document.getElementById('AuctionCalendar');
            var calendar = new FullCalendar.Calendar(calendarEl, {
                initialView: 'dayGridMonth',
                events: function(fetchInfo, successCallback, failureCallback) {
                    fetch('/api/calendar')
                        .then(response => response.json())
                        .then(data => {
                            successCallback(data);
                        })
                        .catch(error => {
                            console.error('Error fetching events:', error);
                            failureCallback(error);
                        });
                },
                timeZone: 'UTC',
                headerToolbar: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'dayGridMonth,timeGridWeek,timeGridDay'
                },
                views: {
                    timeGridWeek: {
                        titleFormat: { year: 'numeric', month: 'long' },
                        eventTimeFormat: { hour: '2-digit', minute: '2-digit', hour12: false } // 24-hour format
                    },
                    timeGridDay: {
                        titleFormat: { year: 'numeric', month: 'long', day: 'numeric' },
                        eventTimeFormat: { hour: '2-digit', minute: '2-digit', hour12: false } // 24-hour format
                    }
                },
                eventTimeFormat: { hour: '2-digit', minute: '2-digit', hour12: false } // 24-hour format
            });
            calendar.render();
        });